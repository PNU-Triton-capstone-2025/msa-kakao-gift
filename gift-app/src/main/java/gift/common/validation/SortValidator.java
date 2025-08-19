package gift.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SortValidator implements ConstraintValidator<ValidSort, Pageable> {
    List<String> validSortProperties = new ArrayList<>();

    @Override
    public void initialize(ValidSort constraintAnnotation){
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

        validSortProperties = Stream.of(enumClass.getEnumConstants())
                .map(e -> e.name().toLowerCase())
                .toList();
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        if (pageable == null || pageable.getSort().isUnsorted()){
            return true;
        }

        Sort sort = pageable.getSort();

        for(Sort.Order order : sort){
            String orderProperty = order.getProperty();
            if(!validSortProperties.contains(orderProperty)){
                return false;
            }
        }
        return true;
    }
}
