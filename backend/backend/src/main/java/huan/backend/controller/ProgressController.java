package huan.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.SubmitResponse;
import huan.backend.service.ProgressService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;
    @GetMapping("/{id}")
    public ResponseEntity<PageResponse<SubmitResponse>> getHistory(@PathVariable Long id){
        return ResponseEntity.ok(progressService.getUserHistory(id, 1, 2));

    }

}
