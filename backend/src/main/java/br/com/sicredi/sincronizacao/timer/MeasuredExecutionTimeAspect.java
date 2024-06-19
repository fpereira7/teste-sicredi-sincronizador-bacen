package br.com.sicredi.sincronizacao.timer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class MeasuredExecutionTimeAspect {

	@Around("@annotation(MeasuredExecutionTime)")
	public Object executionTimeLogger(ProceedingJoinPoint joinPoint) {

		final Logger log = org.slf4j.LoggerFactory.getLogger(MeasuredExecutionTimeAspect.class);

		try {
			StopWatch stopWatch = new StopWatch(joinPoint.getSignature().toString());
			stopWatch.start();
			Object proceed = joinPoint.proceed();
			stopWatch.stop();
			log.error(stopWatch.prettyPrint());
			return proceed;
		} catch (Throwable e) {
			log.error("There was an error while calculating method execution time for {}", joinPoint.getSignature(), e);
			return null;
		}
	}
}