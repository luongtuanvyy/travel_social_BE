package com.app.service.serviceImpl;

import com.app.entity.Account;
import com.app.entity.Blog;
import com.app.modal.BlogModal;
import com.app.payload.request.BlogModalQueryParam;
import com.app.payload.request.BlogQueryParam;
import com.app.payload.response.APIResponse;
import com.app.payload.response.FailureAPIResponse;
import com.app.payload.response.SuccessAPIResponse;
import com.app.repository.AccountRepository;
import com.app.repository.BlogInteractionResponsitory;
import com.app.repository.BlogRepository;
import com.app.security.TokenProvider;
import com.app.service.BlogServices;
import com.app.speficication.BlogModalSpecification;
import com.app.speficication.BlogSpecification;
import com.app.utils.PageUtils;
import com.app.utils.RequestParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServicesImpl implements BlogServices {
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    BlogInteractionResponsitory blogInteractionResponsitory;
    @Autowired
    BlogSpecification blogSpecification;
    @Autowired
    BlogModalSpecification blogModalSpecification;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RequestParamsUtils requestParamsUtils;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    ImportExcelService importExcelService;
    private Specification<BlogModal> spec;

    @Override
    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Optional<Blog> findById(Integer id) {
        return blogRepository.findById(id);
    }

    @Override
    public APIResponse getAllBlogWithAccount(BlogModalQueryParam blogModalQueryParam){
        Specification<BlogModal> spec = blogModalSpecification.getBlogModalSpecification(blogModalQueryParam);
        Pageable pageable = requestParamsUtils.getPageable(blogModalQueryParam);
        Page<BlogModal> response = blogRepository.getAllBlogWithAccount(spec, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse filterBlog(BlogQueryParam blogQueryParam) {
        Specification<Blog> spec = blogSpecification.getBlogSpecification(blogQueryParam);
        Pageable pageable = requestParamsUtils.getPageable(blogQueryParam);
        Page<Blog> response = blogRepository.findAll(spec, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse filterBlogNotSeen(BlogQueryParam blogQueryParam) {
        Specification<Blog> spec = blogSpecification.getBlogSpecification(blogQueryParam);
        Pageable pageable = requestParamsUtils.getPageable(blogQueryParam);
        Page<Blog> response = blogRepository.findAllNotSeen(spec, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse filterLeastBlog(BlogQueryParam blogQueryParam) {
        Specification<Blog> spec = blogSpecification.getBlogSpecification(blogQueryParam);
        Pageable pageable = requestParamsUtils.getPageable(blogQueryParam);
        Page<Blog> response = blogRepository.findLatestBlogs(spec, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse create(Blog blog, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        int id = tokenProvider.getIdFromToken(token);
        Account account = accountRepository.findById(id).orElse(null);
        try {
            if (blog.getTitle() == null || blog.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Blog title cannot be empty");
            }

            blog.setAvatar(account.getAvatar());
            blog.setName(account.getName());
            blog = blogRepository.save(blog);
            return new SuccessAPIResponse(blog);
        } catch (Exception ex) {
            return new FailureAPIResponse(ex.getMessage());
        }
    }

    @Override
    public APIResponse update(Blog blog, HttpServletRequest request) {
        try {

            if (blog == null) {
                return new FailureAPIResponse("Blog id is required!");
            }
            if (blog.getTitle() == null || blog.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Blog title cannot be empty");
            }
            Blog exists = blogRepository.findById(blog.getId()).orElse(null);
            if (exists == null) {
                return new FailureAPIResponse("Cannot find blog with id: " + blog.getId());
            }

            blog = blogRepository.save(blog);
            return new SuccessAPIResponse(blog);
        } catch (Exception ex) {
            return new FailureAPIResponse(ex.getMessage());
        }
    }

    @Override
    public APIResponse delete(Integer id) {
        try {
            blogRepository.deleteById(id);
            return new SuccessAPIResponse("Delete successfully!");
        } catch (Exception ex) {
            return new FailureAPIResponse(ex.getMessage());
        }
    }


    @Override
    public List<Blog> findByTitle(BlogQueryParam blogQueryParam) {
        return null;
    }

    @Override
    public APIResponse uploadExcel(MultipartFile excel) {
        return importExcelService.uploadExcel(excel, Blog.class, blogRepository);
    }

    @Override
    public APIResponse createBatch(List<Blog> blogs) {
        List<Blog> createdBlogs = new ArrayList<>();
        for (Blog blog : blogs) {
            Blog createdBlog = blogRepository.save(blog);
            createdBlogs.add(createdBlog);
        }
        return new SuccessAPIResponse(createdBlogs);
    }

    @Override
    public APIResponse getAccountByBlogId(Integer id, BlogQueryParam blogQueryParam) {
        Blog blog = new Blog();
        Pageable pageable = requestParamsUtils.getPageable(blogQueryParam);
        Page<Blog> response = blogRepository.BlogID(id, pageable);
        if (response.hasContent()) {
            blog.setTitle(response.getContent().get(0).getTitle());
            blog.setAvatar(response.getContent().get(0).getAvatar());
            blog.setName(response.getContent().get(0).getName());
            blog.setDescription(response.getContent().get(0).getDescription());
            blog.setPlaceId(response.getContent().get(0).getPlaceId());
            blog.setTour(response.getContent().get(0).getTour());
            blog.setImage(response.getContent().get(0).getImage());
            blog.setCloudinaryId(response.getContent().get(0).getCloudinaryId());
        }
        blog = blogRepository.save(blog);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        // Lấy header authorization
        String authorization = request.getHeader("Authorization");

        // Kiểm tra header authorization tồn tại và có dạng Bearer <token>
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // Lấy token từ header
            String token = authorization.substring(7);
            // Trả về token
            return token;
        } else {
            // Không có token
            return null;
        }
    }
}
