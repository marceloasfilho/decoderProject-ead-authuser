package com.ead.authuser.client;

import com.ead.authuser.dto.CourseDTO;
import com.ead.authuser.dto.ResponseGetAllCoursesByUserDTO;
import com.ead.authuser.service.UtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;
    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        String url = this.utilsService.buildUrl(userId, pageable);
        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);

        List<CourseDTO> allCoursesByUser;

        try {
            ParameterizedTypeReference<ResponseGetAllCoursesByUserDTO<CourseDTO>> httpResponse = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ResponseGetAllCoursesByUserDTO<CourseDTO>> bodyResponse = this.restTemplate.exchange(url, HttpMethod.GET, null, httpResponse);
            allCoursesByUser = bodyResponse.getBody().getContent();
        } catch (HttpStatusCodeException httpStatusCodeException) {
            allCoursesByUser = null;
            log.error("Error request /course", httpStatusCodeException);
        }

        log.info("Ending request /course userId: {}", userId);

        return new PageImpl<>(allCoursesByUser);
    }
}
