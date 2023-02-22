package com.w2m.sergiojimenez.retow2msjr.annotations;

import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MedicionTiempoEjecucionAspecto {

	private static final Logger LOGGER = Logger.getLogger(MedicionTiempoEjecucionAspecto.class.getName());

	@Around("@annotation(com.w2m.sergiojimenez.retow2msjr.annotations.MedicionTiempoEjecucion)")
	public Object calcularTiempo(ProceedingJoinPoint joinPoint) throws Throwable {

		long tiempoInicial = System.nanoTime();
		Object result = joinPoint.proceed(); // Sección crítica acotada.
		long tiempoFinal = System.nanoTime();

		double tiempoTardado = (double) (tiempoFinal - tiempoInicial) / 1000000;

		String mensaje = "El método '" + joinPoint.getSignature().getName() + "()' de la clase '"
				+ joinPoint.getTarget().getClass().getSimpleName() + "' ha tardado " + tiempoTardado + " ms.";
		if (tiempoTardado > 2000) // Umbral de 2 segs: se pasa a considerar WARNING a eficientar en lugar de INFO.
			LOGGER.warning(mensaje);
		else
			LOGGER.info(mensaje);

		return result;

	}

}
