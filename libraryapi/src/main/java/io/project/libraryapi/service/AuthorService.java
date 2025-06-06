package io.project.libraryapi.service;

import io.project.libraryapi.controller.validator.AuthorValidator;
import io.project.libraryapi.exceptions.NotAllowedOperationException;
import io.project.libraryapi.model.Author;
import io.project.libraryapi.model.User;
import io.project.libraryapi.repository.AuthorRepository;
import io.project.libraryapi.repository.BookRepository;
import io.project.libraryapi.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository repository;
    private final AuthorValidator validator;
    private final SecurityService securityService;
    private final BookRepository bookRepository;

    public Author save(Author author){
        validator.validate(author);
        User user = securityService.findLoggedUser();
        author.setUser(user);
        return repository.save(author);
    }

    public void update(Author author){
        if(author.getId() == null){
            throw new IllegalArgumentException("It's necessary that the author already be saved in the database");
        }
        validator.validate(author);
        repository.save(author);
    }

    public Optional<Author> findById(UUID id){
        return repository.findById(id);
    }

    public void delete(Author author){
        if(hasBook(author)){
            throw new NotAllowedOperationException("It is not allowed to delete an author with registered books!");
        }
        repository.delete(author);
    }

    public List<Author> search(String name, String nationality){
        boolean isNameValid = name != null && !name.trim().isEmpty();
        boolean isNationalityValid = nationality != null && !nationality.trim().isEmpty();

        if (!isNameValid && !isNationalityValid) {
            return List.of();
        }

        if (isNameValid && isNationalityValid){
            return repository.findByNameAndNationality(name, nationality);
        }

        if (isNameValid){
            return repository.findByName(name);
        }

        return repository.findByNationality(nationality);
    }

    public boolean hasBook(Author author){
        return bookRepository.existsByAuthor(author);
    }
}
