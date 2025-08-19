package gift.order.service;

import gift.auth.oauth.event.KakaoOrderCompletedEvent;
import gift.member.domain.Member;
import gift.member.dto.MemberTokenRequest;
import gift.member.repository.MemberRepository;
import gift.order.domain.Order;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.repository.OrderRepository;
import gift.product.domain.ProductOption;
import gift.product.repository.ProductOptionRepository;
import gift.wish.repository.WishRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;
    private final ProductOptionRepository optionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, WishRepository wishRepository, ProductOptionRepository optionRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, MemberTokenRequest memberTokenRequest) {
        Member member = memberRepository.findById(memberTokenRequest.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ProductOption option = optionRepository.findById(requestDto.optionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션입니다."));

        option.subtractQuantity(requestDto.quantity());

        Order order = new Order(option, member, requestDto.quantity(), requestDto.message());
        orderRepository.save(order);

        wishRepository.findByMemberAndProduct(member, option.getProduct())
                .ifPresent(wishRepository::delete);

        if (member.isKakaoUser()) {
            eventPublisher.publishEvent(new KakaoOrderCompletedEvent(member.getKakaoAccessToken(), member.getKakaoRefreshToken(), option.getProduct().getId()));
        }

        return OrderResponseDto.from(order);
    }
}
