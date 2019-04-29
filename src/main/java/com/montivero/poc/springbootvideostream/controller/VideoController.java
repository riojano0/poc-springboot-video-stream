package com.montivero.poc.springbootvideostream.controller;

import com.montivero.poc.springbootvideostream.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VideoController {

	private final VideoService videoService;

	@Autowired
	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("video/{name}/full")
	public ResponseEntity<FileUrlResource> getFullVideo(@PathVariable(value = "name") String name)
			throws IOException {
		return videoService.getFullVideo(name);
	}

	@GetMapping("video/{name}")
	public ResponseEntity<ResourceRegion> getVideo(@PathVariable(value = "name") String name,
												   @RequestHeader("Range") String rangeHeader
	) throws IOException {
		return videoService.getVideoRegion(name, rangeHeader);
	}

}
