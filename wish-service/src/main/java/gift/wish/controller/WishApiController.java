package gift.wish.controller;

import gift.wish.domain.Wish;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishResponse;
import gift.wish.dto.WishUpdateRequest;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishApiController {
    private final WishService wishService;

    public WishApiController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("/{wishId}")
    public ResponseEntity<WishResponse> getWish(
            @RequestHeader("X-Member-Id") Long memberId,
            @PathVariable Long wishId
    ) {
        Wish wish = wishService.getWish(memberId, wishId);
        WishResponse response = new WishResponse(wish.getMemberId(), wish.getProductId(), wish.getQuantity());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@RequestHeader("X-Member-Id") Long memberId, @Valid @RequestBody WishRequest request) {
        wishService.addWish(memberId ,request.productId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<WishListResponse>> getWishes(
            @RequestHeader("X-Member-Id") Long memberId,
            Pageable pageable
    ) {
        List<WishListResponse> wishes = wishService.getWishes(memberId, pageable);
        return ResponseEntity.ok(wishes);
    }

    @PatchMapping("/{wishId}")
    public ResponseEntity<Void> updateWish(@RequestHeader("X-Member-Id") Long memberId, @PathVariable Long wishId, @RequestBody @Valid WishUpdateRequest request){
        wishService.updateQuantity(memberId, wishId, request.quantity());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@RequestHeader("X-Member-Id") Long memberId, @PathVariable Long wishId){
        wishService.deleteWish(memberId, wishId);
        return ResponseEntity.noContent().build();
    }
}
