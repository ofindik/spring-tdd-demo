package de.osmanfindik.springtdddemo.post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
class PostController {

	private final PostRepository postRepository;

	public PostController (PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@GetMapping("")
	List<Post> findAll () {
		return postRepository.findAll ();
	}
}
