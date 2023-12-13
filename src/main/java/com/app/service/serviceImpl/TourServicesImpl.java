package com.app.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.AccountData;
import com.app.entity.Tour;
import com.app.payload.request.TourQueryParam;
import com.app.payload.response.APIResponse;
import com.app.payload.response.FailureAPIResponse;
import com.app.payload.response.SuccessAPIResponse;
import com.app.repository.TourRepository;
import com.app.service.TourServices;
import com.app.speficication.TourSpecification;
import com.app.utils.PageUtils;
import com.app.utils.RequestParamsUtils;

@Service
public class TourServicesImpl implements TourServices {
    @Autowired
    TourRepository tourRepository;

    @Autowired
    RequestParamsUtils requestParamsUtils;

    @Autowired
    TourSpecification tourSpecification;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ImportExcelService importExcelService;
    // @Override
    // public List<Users> findAll() {
    // return userRepository.findAll();
    // }

    // @Override
    // public List<Users> searchUsersByFullName(String searchKeyword) {
    // // Xử lý trước từ khóa tìm kiếm để loại bỏ dấu và chuyển thành chữ thường
    // String processedKeyword =
    // userRepository.removeDiacritics(searchKeyword.toLowerCase());
    // return userRepository.findByFullNameIgnoreCaseContaining(processedKeyword);
    // }
    @Override
    public APIResponse filterTour(TourQueryParam tourQueryParam) {
        Specification<Tour> spec = tourSpecification.getTourSpecification(tourQueryParam);
        Pageable pageable = requestParamsUtils.getPageable(tourQueryParam);
        Page<Tour> response = tourRepository.findAll(spec, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse filterTourDiscount(TourQueryParam tourQueryParam) {
        Specification<Tour> spec = tourSpecification.getTourSpecification(tourQueryParam);
        Pageable pageable = requestParamsUtils.getPageable(tourQueryParam);
        Page<Tour> response = tourRepository.DiscountIsNotNull(spec, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse filterNewlyPosted(TourQueryParam tourQueryParam) {
        Specification<Tour> spec = tourSpecification.isNewlyPosted();
        Pageable pageable = requestParamsUtils.getPageable(tourQueryParam);
        Page<Tour> response = tourRepository.findAll(spec, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse getAccountByTourId(Integer id, TourQueryParam tourQueryParam) {
        Pageable pageable = requestParamsUtils.getPageable(tourQueryParam);
        Page<AccountData> response = tourRepository.getCompanyCreatedBY(id, pageable);
        return new APIResponse(PageUtils.toPageResponse(response));
    }

    @Override
    public APIResponse findbyid(Integer id) {
        Optional<Tour> response = tourRepository.findTourById(id);
        return new APIResponse(response);
    }

    @Override
    public APIResponse create(Tour tour) {
        try {
            if (tour.getName() == null || tour.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Tour name cannot be empty");
            }
            tour = tourRepository.save(tour);
            return new SuccessAPIResponse(tour);
        } catch (Exception ex) {
            return new FailureAPIResponse(ex.getMessage());
        }
    }

    @Override
    public APIResponse update(Tour tour) {
        try {
            if (tour == null) {
                return new FailureAPIResponse("tour id is required!");
            }
            if (tour.getName() == null || tour.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Tour name cannot be empty");
            }
            Tour exists = tourRepository.findById(tour.getId()).orElse(null);
            if (exists == null) {
                return new FailureAPIResponse("Cannot find tour with id: " + tour.getId());
            }
            tour = tourRepository.save(tour);
            return new SuccessAPIResponse(tour);
        } catch (Exception ex) {
            return new FailureAPIResponse(ex.getMessage());
        }
    }

    @Override
    public APIResponse delete(Integer id) {
        try {
            tourRepository.deleteById(id);
            return new SuccessAPIResponse("Delete successfully!");
        } catch (Exception ex) {
            return new FailureAPIResponse(ex.getMessage());
        }
    }

    @Override
    public APIResponse uploadExcel(MultipartFile excel) {
        return importExcelService.uploadExcel(excel, Tour.class, tourRepository);
    }

    @Override
    public APIResponse createBatch(List<Tour> tours) {
        List<Tour> createdTours = new ArrayList<>();
        for (Tour tour : tours) {
            Tour createdTour = tourRepository.save(tour);
            createdTours.add(createdTour);
        }
        return new SuccessAPIResponse(createdTours);
    }

    @Override
    public APIResponse getRevenueTour(Integer id) {
        Optional<Integer> revenue = tourRepository.getRevenueTour(id);
        return new SuccessAPIResponse(revenue.orElse(0));
    }

}
