package com.example.siteDiscoBackend.Genre;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
}
