package com.app.api;

import com.app.entity.Favorite;
import com.app.entity.Follow;
import com.app.payload.request.FollowQueryParam;
import com.app.payload.response.APIResponse;
import com.app.service.FollowServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FollowApi {
    @Autowired
    FollowServices followServices;

    @GetMapping("/user/follows")
    public ResponseEntity<?> getAllFollow(FollowQueryParam followQueryParam) {
        return ResponseEntity.ok(followServices.filterFollow(followQueryParam));
    }

    @PostMapping("/user/follows")
    public ResponseEntity<?> createFollow(@RequestPart(name = "follow") Follow follow) {
        APIResponse response = followServices.create(follow);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/user/follows")
    public ResponseEntity<?> updateFollow(@RequestPart(name = "follow") Follow follow) {
        APIResponse response = followServices.update(follow);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/user/follows")
    public ResponseEntity<?> deleteFollow(@RequestParam("id") Integer id) {
        APIResponse response = followServices.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/user/follows/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestPart(name = "excel") MultipartFile excel) {
        APIResponse response = followServices.uploadExcel(excel);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/user/follows/batch")
    public ResponseEntity<?> createFollowsBatch(@RequestBody List<Follow> follows) {
        APIResponse response = followServices.createBatch(follows);
        return ResponseEntity.ok().body(response);
    }
}
