package de.osmanfindik.springtdddemo.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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
		when (postRepository.findAll ()).thenReturn (posts);

		var post1 = posts.get (0);
		var post2 = posts.get (1);
		var jsonResponse = STR. """
							[
								{
										"id":\{ post1.id () },
										"userId":\{ post1.userId () },
										"title":"\{ post1.title () }",
										"body":"\{ post1.body () }",
										"version":null
								},
								{
										"id":\{ post2.id () },
										"userId":\{ post2.userId () },
										"title":"\{ post2.title () }",
										"body":"\{ post2.body () }",
										"version":null
								}
							]
				""" ;
		mockMvc.perform (get ("/api/posts"))
				.andExpect (status ().isOk ())
				.andExpect (content ().json (jsonResponse));
	}

	@Test
	void shouldfindPostWhenValidIDIsGiven () throws Exception {
		when (postRepository.findById (1)).thenReturn (Optional.of (posts.get (0)));

		var post = posts.get (0);
		var jsonResponse = STR. """
							{
								"id":\{ post.id () },
								"userId":\{ post.userId () },
								"title":"\{ post.title () }",
								"body":"\{ post.body () }",
								"version":null
							}
				""" ;
		mockMvc.perform (get ("/api/posts/1"))
				.andExpect (status ().isOk ())
				.andExpect (content ().json (jsonResponse));

	}
}
