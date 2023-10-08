package de.osmanfindik.springtdddemo.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	PostRepository postRepository;

	List<Post> posts;

	@BeforeEach
	void setUp () {
		posts = List.of (
				new Post (1, 1, "First Post", "This is first post", null),
				new Post (2, 1, "Second Post", "This is second post", null)
		);
	}

	@Test
	void shouldFineAllPosts () throws Exception {
		String jsonResponse = """
							[
								{
										"id":1,
										"userId":1,
										"title":"First Post",
										"body":"This is first post"
								},
								{
										"id":2,
										"userId":1,
										"title":"Second Post",
										"body":"This is second post"
								}
							]
				""";

		when (postRepository.findAll ()).thenReturn (posts);
		mockMvc.perform (get ("/api/posts"))
				.andExpect (status ().isOk ())
				.andExpect (content ().json (jsonResponse));

	}
}
