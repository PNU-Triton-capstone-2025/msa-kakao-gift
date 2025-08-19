package gift.wish.controller;

import gift.auth.Login;
import gift.common.enums.ProductSortProperty;
import gift.common.enums.WishSortProperty;
import gift.common.validation.ValidSort;
import gift.member.dto.MemberTokenRequest;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishUpdateRequest;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<Void> addWish(@Login MemberTokenRequest memberTokenRequest, @Valid @RequestBody WishRequest request) {
        wishService.addWish(memberTokenRequest ,request.productId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<WishListResponse>> getWishes(
            @Login MemberTokenRequest memberTokenRequest,
            @ValidSort(enumClass = WishSortProperty.class)
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<WishListResponse> wishes = wishService.getWishes(memberTokenRequest, pageable);

        return ResponseEntity.ok(wishes);
    }

    @PatchMapping("/{wishId}")
    public ResponseEntity<Void> updateWish(@Login MemberTokenRequest memberTokenRequest, @PathVariable Long wishId, @RequestBody WishUpdateRequest request){
        wishService.updateQuantity(memberTokenRequest, wishId, request.quantity());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@Login MemberTokenRequest memberTokenRequest, @PathVariable Long wishId){
        wishService.deleteWish(memberTokenRequest, wishId);
        return ResponseEntity.noContent().build();
    }
}
