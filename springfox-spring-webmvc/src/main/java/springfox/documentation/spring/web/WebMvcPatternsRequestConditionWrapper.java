/*
 *
 *  Copyright 2015 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.documentation.spring.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.spring.wrapper.PatternsRequestCondition;
import springfox.documentation.spring.wrapper.RequestCondition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static springfox.documentation.spring.web.paths.Paths.maybeChompLeadingSlash;
import static springfox.documentation.spring.web.paths.Paths.maybeChompTrailingSlash;

public class WebMvcPatternsRequestConditionWrapper
		implements springfox.documentation.spring.wrapper.RequestCondition<RequestCondition> {

	Log log = LogFactory.getLog(WebMvcPatternsRequestConditionWrapper.class);

	private String contextPath;
	private RequestCondition condition;

	public WebMvcPatternsRequestConditionWrapper(
			String contextPath,
			RequestCondition condition,
			RequestMappingInfo requestMappingInfo) {

		this.init(contextPath, condition, requestMappingInfo);
	}

	public WebMvcPatternsRequestConditionWrapper(String contextPath, String... patterns){
		// 模拟构建PatternsRequestCondition
		PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(){
			@Override
			public RequestCondition combine(RequestCondition other) {
				return null;
			}

			@Override
			public PatternsRequestCondition combine(PatternsRequestCondition other) {
				return null;
			}

			@Override
			public Set<String> getPatterns() {
				return new HashSet<>(Arrays.asList(patterns));
			}
		};
		this.init(contextPath, patternsRequestCondition, null);
	}

	/**
	 * 初始化
	 * @param
	 * @date 2023/6/9
	 * @author Kenneth
	 */
	private void init(String contextPath,
					  RequestCondition condition,
					  RequestMappingInfo requestMappingInfo){
		this.contextPath = contextPath;
		if (condition != null) {
			this.condition = condition;
		} else {
			PathPatternsRequestCondition pathPatternsRequestCondition = requestMappingInfo.getPathPatternsCondition();
			boolean isPathPattern = pathPatternsRequestCondition != null && !pathPatternsRequestCondition.getPatterns().isEmpty();
			if (isPathPattern) {
				this.condition = SpringWebMvcUtils.coverPathPatternsCondition(pathPatternsRequestCondition);
			} else {
				this.condition = SpringWebMvcUtils.coverPatternsCondition(requestMappingInfo.getPatternsCondition());
			}
		}
	}


	@Override
	public springfox.documentation.spring.wrapper.RequestCondition combine(
			springfox.documentation.spring.wrapper.RequestCondition<RequestCondition> other) {
		if (other instanceof WebMvcPatternsRequestConditionWrapper && !this.equals(other)) {
			return new WebMvcPatternsRequestConditionWrapper(
					contextPath,
					condition.combine(((WebMvcPatternsRequestConditionWrapper) other).condition),
					null);
		}
		return this;
	}

	@Override
	public Set<String> getPatterns() {
		Set<String> patterns = this.condition.getPatterns();
		if(patterns != null && !patterns.isEmpty()){
			patterns = patterns.stream()
					.map(o -> String.format("%s/%s", maybeChompTrailingSlash(contextPath),  maybeChompLeadingSlash(o)))
					.collect(Collectors.toSet());
		}
		return patterns;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof WebMvcPatternsRequestConditionWrapper) {
			return this.condition.equals(((WebMvcPatternsRequestConditionWrapper) o).condition);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.condition.hashCode();
	}


	@Override
	public String toString() {
		return this.condition.toString();
	}
}

