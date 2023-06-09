package springfox.documentation.spring.web;

import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;
import springfox.documentation.spring.wrapper.RequestCondition;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kenneth
 * @date 2023/6/8
 * @desciption
 **/
public class SpringWebMvcUtils {


	/**
	 * 获取RequestMapping的Path
	 * @param requestMappingInfo 请求信息
	 * @date 2023/6/8
	 * @author Kenneth
	 */
	public static Set<String> getRequestMappingPath(RequestMappingInfo requestMappingInfo) {
		if (requestMappingInfo.getPathPatternsCondition() == null) {
			return getPatterns(requestMappingInfo.getPatternsCondition());
		} else {
			return getPathPatterns(requestMappingInfo.getPathPatternsCondition());
		}
	}

	/**
	 * 获取PathPatternParser模式下的Path
	 * @param patternsRequestCondition
	 * @date 2023/6/8
	 * @author Kenneth
	 */
	public static Set<String> getPatterns(PatternsRequestCondition patternsRequestCondition){
		if(patternsRequestCondition == null){
			return new HashSet<>(0);
		}
		Set<String> patterns = patternsRequestCondition.getPatterns();
		return patterns;
	}

	/**
	 * 获取AntPathPatterns模式下的Path
	 * @param pathPatternsRequestCondition
	 * @date 2023/6/8
	 * @author Kenneth
	 */
	public static Set<String> getPathPatterns(PathPatternsRequestCondition pathPatternsRequestCondition){
		if(pathPatternsRequestCondition == null){
			return new HashSet<>(0);
		}
		Set<PathPattern> patterns = pathPatternsRequestCondition.getPatterns();
		return patterns.stream().map(PathPattern::getPatternString).collect(java.util.stream.Collectors.toSet());
	}

	/**
	 * PathPatterns转换
	 * @param pathPatternsRequestCondition
	 * @date 2023/6/9
	 * @author Kenneth
	 */
	public static springfox.documentation.spring.wrapper.PathPatternsRequestCondition coverPathPatternsCondition(PathPatternsRequestCondition pathPatternsRequestCondition){
		springfox.documentation.spring.wrapper.PathPatternsRequestCondition condition = new springfox.documentation.spring.wrapper.PathPatternsRequestCondition() {
			@Override
			public springfox.documentation.spring.wrapper.PathPatternsRequestCondition combine(springfox.documentation.spring.wrapper.PathPatternsRequestCondition other) {
				return null;
			}

			@Override
			public Set<String> getPatterns() {
				return pathPatternsRequestCondition.getPatterns().stream()
						.map(PathPattern::getPatternString)
						.collect(java.util.stream.Collectors.toSet());
			}

			@Override
			public RequestCondition combine(RequestCondition other) {
				return null;
			}

		};
		return condition;
	}

	/**
	 * Patterns转换
	 * @param
	 * @date 2023/6/9
	 * @author Kenneth
	 */
	public static springfox.documentation.spring.wrapper.PatternsRequestCondition coverPatternsCondition(PatternsRequestCondition patternsRequestCondition){
		springfox.documentation.spring.wrapper.PatternsRequestCondition condition = new springfox.documentation.spring.wrapper.PatternsRequestCondition() {
			@Override
			public springfox.documentation.spring.wrapper.PatternsRequestCondition combine(springfox.documentation.spring.wrapper.PatternsRequestCondition other) {
				return null;
			}

			@Override
			public Set<String> getPatterns() {
				return patternsRequestCondition.getPatterns();
			}

			@Override
			public RequestCondition combine(RequestCondition other) {
				return null;
			}

		};
		return condition;
	}
}
