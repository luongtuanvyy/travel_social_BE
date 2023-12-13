package com.app.service;

import com.app.entity.Blog;
import com.app.modal.BlogModal;
import com.app.payload.request.BlogModalQueryParam;
import com.app.payload.request.BlogQueryParam;
import com.app.payload.response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface BlogServices {
    List<Blog> findAll();
    Optional<Blog> findById(Integer id);

    APIResponse getAllBlogWithAccount(BlogModalQueryParam blogModalQueryParam);

    APIResponse filterBlog(BlogQueryParam blogQueryParam);

    APIResponse filterBlogNotSeen(BlogQueryParam blogQueryParam);

    APIResponse filterLeastBlog(BlogQueryParam blogQueryParam);

    APIResponse create(Blog blog, HttpServletRequest request);

    APIResponse update(Blog blog, HttpServletRequest request);

    APIResponse delete(Integer id);

    List<Blog> findByTitle(BlogQueryParam blogQueryParam);
    APIResponse uploadExcel(MultipartFile excel);
    APIResponse createBatch(List<Blog> blogs);

    APIResponse getAccountByBlogId(Integer id, BlogQueryParam blogQueryParam);

}
