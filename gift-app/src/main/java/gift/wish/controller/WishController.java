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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/wishes")
public class WishController {
    private WishService wishService;

    public WishController(WishService wishService){
        this.wishService = wishService;
    }

    @GetMapping
    public String getWishes(
            @Login MemberTokenRequest memberTokenRequest,
            @ValidSort(enumClass = WishSortProperty.class)
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<WishListResponse> wishes = wishService.getWishes(memberTokenRequest, pageable);

        model.addAttribute("wishes", wishes);
        return "wishes/wishes";
    }

    @PostMapping("/add")
    public String addWish(@Login MemberTokenRequest memberTokenRequest, @ModelAttribute WishRequest request) {
        wishService.addWish(memberTokenRequest, request.productId());
        return "redirect:/wishes";
    }

    @PostMapping("/update/{wishId}")
    public String updateWish(@Login MemberTokenRequest memberTokenRequest, @PathVariable Long wishId, @ModelAttribute WishUpdateRequest request){
        wishService.updateQuantity(memberTokenRequest, wishId, request.quantity());

        return "redirect:/wishes";
    }

    @PostMapping("/delete/{wishId}")
    public String deleteWish(@Login MemberTokenRequest memberTokenRequest, @PathVariable("wishId") Long wishId) {
        wishService.deleteWish(memberTokenRequest, wishId);
        return "redirect:/wishes";
    }
}
