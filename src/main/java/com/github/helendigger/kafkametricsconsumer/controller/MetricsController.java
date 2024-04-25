package com.github.helendigger.kafkametricsconsumer.controller;

import com.github.helendigger.kafkametricsconsumer.dto.MetricViewDTO;
import com.github.helendigger.kafkametricsconsumer.service.MetricsService;
import com.github.helendigger.kafkametricsconsumer.view.ListMetricsView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class MetricsController {
    private MetricsService metricsService;

    /**
     * Get list of all metrics
     * @return list of all metrics
     */
    @Operation(summary = "Get all metrics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all metrics", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ListMetricsView.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping(value = "/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListMetricsView> getAllMetrics() {
        return ResponseEntity.ok(new ListMetricsView(metricsService.getAllMetrics()));
    }

    /**
     * Get metrics by ID
     * @param id id of the metric
     * @return 200 and entity or 404 if not found
     */
    @Operation(summary = "Get metric by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metric got by id", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MetricViewDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Metric not found by id", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping(value = "/metrics/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MetricViewDTO> getMetricById(@PathVariable @Validated @Min(1) Long id) {
        return metricsService.getMetricStatisticById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Handle validation errors, return bad request and a map with invalid values
     * @param exception exception to handle
     * @return response entity with bad request status and a map of invalid values and reason why they are invalid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRequestArguments(MethodArgumentNotValidException exception) {
        var validationErrors = exception.getBindingResult().getAllErrors().stream()
                .filter(FieldError.class::isInstance)
                .map(FieldError.class::cast)
                .collect(Collectors.toMap(FieldError::getField, e -> Optional.ofNullable(e.getDefaultMessage())
                        .orElseGet(() -> "Invalid")));
        return ResponseEntity.badRequest().body(validationErrors);
    }

    /**
     * Handle argument error, return bad request and an object {"error" : "description"} back to user
     * @param exception exception to handle
     * @return response entity with bad request status and object with error
     */
    @ExceptionHandler(value = {MethodArgumentConversionNotSupportedException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, String>> handleInvalidConversion(Exception exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    /**
     * Every other error that is not validation or parsing request should be treated as Internal
     * @param throwable error that occurred inside the service
     * @return response entity with internal server error and object with error
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleGenericException(Throwable throwable) {
        return ResponseEntity.internalServerError().body(Map.of("error", throwable.getMessage()));
    }
}
