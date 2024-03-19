package com.mukku.shrinkurl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.mukku.shrinkurl.config.ShorteningUtil;
import com.mukku.shrinkurl.dto.FullUrl;
import com.mukku.shrinkurl.dto.ShortUrl;
import com.mukku.shrinkurl.dto.UrlDto;
import com.mukku.shrinkurl.entity.Url;
import com.mukku.shrinkurl.repository.UrlRepository;

@Service
public class UrlService {

    Logger logger = LoggerFactory.getLogger(UrlService.class);

    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @SuppressWarnings("null")
    private Url get(Long id) {
        logger.info(String.format("Fetching Url from database for Id %d", id));
        Url url = urlRepository.findById(id).get();
        return url;
    }

    public FullUrl getFullUrl(String shortenString) {
        logger.debug("Converting Base 62 string %s to Base 10 id");
        Long id = ShorteningUtil.strToId(shortenString);
        logger.info(String.format("Converted Base 62 string %s to Base 10 id %s", shortenString, id));

        logger.info(String.format("Retrieving full url for %d", id));
        return new FullUrl(this.get(id).getFullUrl());
    }

    private Url save(FullUrl fullUrl) {
        return urlRepository.save(new Url(fullUrl.getFullUrl()));
    }

    public ShortUrl getShortUrl(FullUrl fullUrl) {

        logger.info("Checking if the url already exists");
        List<Url> savedUrls = null;
        savedUrls = checkFullUrlAlreadyExists(fullUrl);

        Url savedUrl = null;

        if (savedUrls.isEmpty()) {
            logger.info(String.format("Saving Url %s to database", fullUrl.getFullUrl()));
            savedUrl = this.save(fullUrl);
            logger.debug(savedUrl.toString());
        }
        else {
            savedUrl = savedUrls.get(0);
            logger.info(String.format("url: %s already exists in the database. skipped insert", savedUrl));
        }

        logger.debug(String.format("Converting Base 10 %d to Base 62 string", savedUrl.getId()));
        String shortUrlText = ShorteningUtil.idToStr(savedUrl.getId());
        logger.info(String.format("Converted Base 10 %d to Base 62 string %s", savedUrl.getId(), shortUrlText));

        return new ShortUrl(shortUrlText);
    }

    private List<Url> checkFullUrlAlreadyExists(FullUrl fullUrl) {
        return urlRepository.findUrlByFullUrl(fullUrl.getFullUrl());
    }

    public List<UrlDto> findAllUrl(String user) {
        List<Url> urls = urlRepository.findAllUrls(user);
        return urls.stream().map((url) -> convertEntityToDto(url))
                .collect(Collectors.toList());
    }

    private UrlDto convertEntityToDto(Url url){
        UrlDto urlDto = new UrlDto();
        urlDto.setFullUrl(url.getFullUrl());
        urlDto.setShortUrl(url.getShortUrl());
        urlDto.setUser(url.getUser());
        return urlDto;
    }
}
