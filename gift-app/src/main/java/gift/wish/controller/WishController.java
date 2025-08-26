package gift.wish.controller;

import gift.auth.AuthUtil;
import gift.auth.Login;
import gift.common.enums.WishSortProperty;
import gift.common.validation.ValidSort;
import gift.member.dto.MemberTokenRequest;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishUpdateRequest;
import gift.wish.service.WishService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final WishService wishService;

    public WishController(WishService wishService){
        this.wishService = wishService;
    }

    @GetMapping
    public String getWishes(
            @ValidSort(enumClass = WishSortProperty.class)
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            HttpServletRequest request
    ) {
        String token = AuthUtil.extractToken(request);
        Page<WishListResponse> wishes = wishService.getWishes(pageable, token);

        model.addAttribute("wishes", wishes);
        return "wishes/wishes";
    }

    @PostMapping("/add")
    public String addWish(HttpServletRequest request, @ModelAttribute WishRequest wishRequest) {
        String token = AuthUtil.extractToken(request);
        wishService.addWish(wishRequest, token);
        return "redirect:/wishes";
    }

    @PostMapping("/update/{wishId}")
    public String updateWish(HttpServletRequest request, @PathVariable Long wishId, @ModelAttribute WishUpdateRequest wishUpdateRequest){
        String token = AuthUtil.extractToken(request);
        wishService.updateQuantity(wishId, wishUpdateRequest, token);
        return "redirect:/wishes";
    }

    @PostMapping("/delete/{wishId}")
    public String deleteWish(HttpServletRequest request, @PathVariable("wishId") Long wishId) {
        String token = AuthUtil.extractToken(request);
        wishService.deleteWish(wishId, token);
        return "redirect:/wishes";
    }
}
