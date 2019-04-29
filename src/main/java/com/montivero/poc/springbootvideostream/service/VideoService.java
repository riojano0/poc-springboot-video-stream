package com.montivero.poc.springbootvideostream.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.lang.Math.min;

@Component
public class VideoService {

	private static final long CHUNK_SIZE = 1000000L;
	private final String videoLocation;

	@Autowired
	public VideoService(@Value("${video.location}") String videoLocation) {
		this.videoLocation = videoLocation;
	}

	public ResponseEntity<FileUrlResource> getFullVideo(String videoName) throws IOException {
		FileUrlResource videoResource = new FileUrlResource(videoLocation + "/" + videoName);

		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(videoResource)
						.orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(videoResource);
	}

	public ResponseEntity<ResourceRegion> getVideoRegion(String videoName, String rangeHeader) throws IOException {
		FileUrlResource videoResource = new FileUrlResource(videoLocation + "/" + videoName);
		ResourceRegion resourceRegion = getResourceRegion(videoResource, rangeHeader);

		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(videoResource)
						.orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(resourceRegion);
	}

	private ResourceRegion getResourceRegion(UrlResource video, String httpHeaders) throws IOException {
		ResourceRegion resourceRegion;
		long contentLength = video.contentLength();
		int fromRange = 0;
		int toRange = 0;

		if (StringUtils.isNotBlank(httpHeaders)) {
			String[] ranges = httpHeaders.substring("bytes=".length()).split("-");
			fromRange = Integer.valueOf(ranges[0]);
			toRange = Integer.valueOf(ranges[1]);
		}

		if (fromRange > 0) {
			long rangeLength = min(CHUNK_SIZE, toRange - fromRange + 1);
			resourceRegion = new ResourceRegion(video, fromRange, rangeLength);
		} else {
			long rangeLength = min(CHUNK_SIZE, contentLength);
			resourceRegion = new ResourceRegion(video, 0, rangeLength);
		}

		return resourceRegion;
	}

}
