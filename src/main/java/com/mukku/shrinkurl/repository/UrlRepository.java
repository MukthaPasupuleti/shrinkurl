package com.mukku.shrinkurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mukku.shrinkurl.entity.Url;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    @Query("SELECT u FROM url u WHERE u.fullUrl = ?1")
    List<Url> findUrlByFullUrl(String fullUrl);

    @Query("SELECT u FROM url u WHERE u.user = ?1")
    List<Url> findAllUrls(String user);
}
