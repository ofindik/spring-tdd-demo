package de.osmanfindik.springtdddemo.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
	void shouldFindPostWhenValidIdIsGiven () throws Exception {
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

	@Test
	void shouldNotFindPostWhenInvalidIdIsGiven () throws Exception {
		when (postRepository.findById (999)).thenThrow (PostNotFoundException.class);

		mockMvc.perform (get ("/api/posts/999"))
				.andExpect (status ().isNotFound ());
	}

	@Test
	void shouldDefineNewPostWhenPostIsValid () throws Exception {
		var post = new Post (3, 1, "NEW TITLE", "NEW BODY", null);
		when (postRepository.save (post)).thenReturn (post);
		var json = STR. """
							{
								"id":\{ post.id () },
								"userId":\{ post.userId () },
								"title":"\{ post.title () }",
								"body":"\{ post.body () }",
								"version":null
							}
							""" ;

		mockMvc.perform (post ("/api/posts")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json))
				.andExpect (status ().isCreated ());
	}

	@Test
	void shouldNotDefineNewPostWhenPostIsInvalid () throws Exception {
		var post = new Post (3, 1, "", "", null);
		when (postRepository.save (post)).thenReturn (post);
		var json = STR. """
							{
								"id":\{ post.id () },
								"userId":\{ post.userId () },
								"title":"\{ post.title () }",
								"body":"\{ post.body () }",
								"version":null
							}
							""" ;

		mockMvc.perform (post ("/api/posts")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json))
				.andExpect (status ().isBadRequest ());
	}

	@Test
	void shouldUpdatePostWhenPostIsInvalid () throws Exception {
		Post updated = new Post (1, 1, "This is a new title", "This is a new body", 1);
		when (postRepository.findById (1)).thenReturn (Optional.of (updated));
		when (postRepository.save (updated)).thenReturn (updated);

		var json = STR. """
							{
								"id":\{ updated.id () },
								"userId":\{ updated.userId () },
								"title":"\{ updated.title () }",
								"body":"\{ updated.body () }",
								"version":\{ updated.version () }
							}
							""" ;
		mockMvc.perform (put ("/api/posts/1")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json))
				.andExpect (status ().isOk ());
	}

	@Test
	void shouldDeletePostWhenIdIsValid () throws Exception {
		doNothing ().when (postRepository).deleteById (1);

		mockMvc.perform (delete ("/api/posts/1"))
				.andExpect (status ().isNoContent ());

		verify (postRepository, times (1)).deleteById (1);
	}
}
