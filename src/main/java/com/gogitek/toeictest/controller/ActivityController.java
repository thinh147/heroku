package com.gogitek.toeictest.controller;

import com.gogitek.toeictest.config.response.ResponseEntityBuilder;
import com.gogitek.toeictest.controller.route.ActivitiesRoute;
import com.gogitek.toeictest.controller.route.BaseRoute;
import com.gogitek.toeictest.service.ActivitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BaseRoute.BASE_ENDPOINT + BaseRoute.ACTIVITIES_BASE)
@RequiredArgsConstructor
public class ActivityController {
    private final ActivitiesService activitiesService;

    @GetMapping(ActivitiesRoute.VOCAB_GROUP_URL)
    public ResponseEntity<?> retrieveListVocabGroup(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                    @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(activitiesService.retrieveGroupPage(offset, limit))
                .build();
    }

    @GetMapping(ActivitiesRoute.TYPE_URL)
    public ResponseEntity<?> getListTestType() {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(activitiesService.retrieveExamType())
                .build();
    }

    @GetMapping(ActivitiesRoute.LIST_QUESTION)
    public ResponseEntity<?> getListQuestionByType(@PathVariable Long typeId,
                                                   @RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                   @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(activitiesService.retrieveQuestionByTypeId(typeId, offset, limit))
                .build();
    }
}
