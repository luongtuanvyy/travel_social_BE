package com.app.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.app.payload.request.BlogModalQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Blog;
import com.app.payload.request.BlogQueryParam;
import com.app.payload.response.APIResponse;
import com.app.service.BlogServices;

@RestController
@RequestMapping("/api")
public class BlogApi {
    @Autowired
    BlogServices blogServices;

    @PostMapping("/user/blogs/share")
    public ResponseEntity<?> getAccount(@RequestParam("id") Integer id, BlogQueryParam blogQueryParam) {
        return ResponseEntity.ok(blogServices.getAccountByBlogId(id, blogQueryParam));
    }

//    @GetMapping("/public/blogs")
//    public ResponseEntity<?> getAllBlog(BlogQueryParam blogQueryParam) {
//        return ResponseEntity.ok(blogServices.filterBlog(blogQueryParam));
//    }

    @GetMapping("/public/blogs")
    public ResponseEntity<?> getAllBlog(BlogModalQueryParam blogModalQueryParam) {
        return ResponseEntity.ok(blogServices.getAllBlogWithAccount(blogModalQueryParam));
    }

    @GetMapping("/public/blogs/notSeen")
    public ResponseEntity<?> filterBlogNotSeen(BlogQueryParam blogQueryParam) {
        return ResponseEntity.ok(blogServices.filterBlogNotSeen(blogQueryParam));
    }

    @GetMapping("/public/blogs/least")
    public ResponseEntity<?> filterLeastBlog(BlogQueryParam blogQueryParam) {
        return ResponseEntity.ok(blogServices.filterLeastBlog(blogQueryParam));
    }

    @PostMapping("/user/blogs")
    public ResponseEntity<?> createBlog(@RequestPart(name = "blog") Blog blog,
                                        HttpServletRequest request) {
        APIResponse response = blogServices.create(blog, request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/user/blogs")
    public ResponseEntity<?> updateBlog(@RequestPart(name = "blog") Blog blog,
                                        HttpServletRequest request) {
        APIResponse response = blogServices.update(blog, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/user/blogs")
    public ResponseEntity<?> deleteBlog(@RequestParam("id") Integer id) {
        APIResponse response = blogServices.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/user/blogs/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestPart(name = "excel") MultipartFile excel) {
        APIResponse response = blogServices.uploadExcel(excel);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/user/blogs/batch")
    public ResponseEntity<?> createBlogsBatch(@RequestBody List<Blog> blogs) {
        APIResponse response = blogServices.createBatch(blogs);
        return ResponseEntity.ok().body(response);
    }

}
