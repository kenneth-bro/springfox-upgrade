package springfox.documentation.spring.wrapper;

import java.util.Set;

/**
 * RequestCondition父类
 * @date 2023/6/9
 * @author Kenneth
 */
public interface RequestCondition<T> {

	RequestCondition combine(RequestCondition<T> other);

	Set<String> getPatterns();

}
