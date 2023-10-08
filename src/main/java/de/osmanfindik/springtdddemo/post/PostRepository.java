package de.osmanfindik.springtdddemo.post;

import org.springframework.data.repository.ListCrudRepository;

public interface PostRepository extends ListCrudRepository<Post, Integer> {

}
