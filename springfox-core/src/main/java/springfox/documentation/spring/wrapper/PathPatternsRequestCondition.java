package springfox.documentation.spring.wrapper;

import org.springframework.web.util.pattern.PathPattern;
import java.util.Set;

/**
 * @author Kenneth
 * @date 2023/6/8
 * @desciption
 **/
public interface PathPatternsRequestCondition<T> extends RequestCondition<T>{

	PathPatternsRequestCondition combine(PathPatternsRequestCondition<T> other);

}
