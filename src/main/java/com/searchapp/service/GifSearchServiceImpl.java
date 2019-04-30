package com.searchapp.service;

import com.searchapp.dto.Gif;
import com.searchapp.dto.GiphyDto;
import com.searchapp.dto.GiphyResponse;
import com.searchapp.util.DataTransferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * core search impl
 * logic
 * 1) Do internal solr search
 * 2) call giphy search if internal search result < 5
 * 3) post giphy response in kafka for indexing - async post
 */
@Service
public class GifSearchServiceImpl implements GifSearch {

    final
    ExternalSearchImpl giphySearch;

    @Value(("${giphy.api.record-count}"))
    private String nofMinRecords;

    private final Logger logger = LoggerFactory.getLogger(GifSearchServiceImpl.class);

    @Autowired
    public GifSearchServiceImpl(ExternalSearchImpl giphySearch) {
        this.giphySearch = giphySearch;
    }

    @Override
    public List<Gif> search(String query) {

        logger.debug("search query:{}", query);
        int maxRecords = Integer.valueOf(nofMinRecords);

        logger.info("]start - giphy search");
        GiphyResponse giphyResponse = giphySearch.doGiphySearch(query);

        List<Gif> result = null;
        if (giphyResponse == null || CollectionUtils.isEmpty(giphyResponse.getData())) {
            logger.info("Giphy response is null or empty");
        } else {
            List<GiphyDto> giphyDtos = giphyResponse.getData();
            if (giphyDtos.size() >= maxRecords) {
                result = DataTransferUtil.convertGiphyToGif(giphyDtos);
            }
        }

        return result;
    }



}
