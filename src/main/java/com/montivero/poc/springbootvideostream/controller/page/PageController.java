package com.montivero.poc.springbootvideostream.controller.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PageController {

	private final String videoLocation;

	@Autowired
	public PageController(@Value("${video.location}") String videoLocation) {
		this.videoLocation = videoLocation;
	}

	@GetMapping("/")
	public String index(Model model) throws IOException {
		List<String> videos = Files.list(Paths.get(videoLocation))
				.map(video -> video.getFileName().toString())
				.collect(Collectors.toList());
		model.addAttribute("videos", videos);

		return "index";
	}

	@GetMapping("/{videoName}")
	public String index(@PathVariable(value = "videoName") String videoName, Model model) throws IOException {
		model.addAttribute("videoName", videoName);

		return "video";
	}

}
