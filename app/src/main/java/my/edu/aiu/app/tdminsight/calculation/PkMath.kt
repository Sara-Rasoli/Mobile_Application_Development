package my.edu.aiu.app.tdminsight.calculation

import kotlin.math.exp
import kotlin.math.ln

data class CurvePoint(
    val timeHr: Double,
    val concentrationMgL: Double
)

object PkMath {

    /**
     * Calculates steady-state peak concentration at the end of infusion (t = tinf).
     */
    fun calculateCmaxSs(
        doseMg: Double,
        tinf: Double,
        tau: Double,
        ke: Double,
        vd: Double
    ): Double {
        require(tinf > 0) { "Infusion duration must be > 0" }
        require(tau > 0) { "Dosing interval must be > 0" }
        require(ke > 0) { "Ke must be > 0" }
        require(vd > 0) { "Vd must be > 0" }

        val k0 = doseMg / tinf
        return (k0 / (ke * vd)) * ((1.0 - exp(-ke * tinf)) / (1.0 - exp(-ke * tau)))
    }

    /**
     * Calculates steady-state trough concentration at the end of dosing interval (t = tau).
     */
    fun calculateCminSs(
        cmaxSs: Double,
        ke: Double,
        tau: Double,
        tinf: Double
    ): Double {
        require(ke > 0) { "Ke must be > 0" }
        require(tau >= tinf) { "Dosing interval must be >= infusion duration" }

        return cmaxSs * exp(-ke * (tau - tinf))
    }

    /**
     * Calculates steady-state concentration at time t since start of infusion (0 <= t <= tau).
     */
    fun calculateConcentration(
        doseMg: Double,
        tinf: Double,
        tau: Double,
        ke: Double,
        vd: Double,
        t: Double
    ): Double {
        require(tinf > 0) { "Infusion duration must be > 0" }
        require(tau > 0) { "Dosing interval must be > 0" }
        require(ke > 0) { "Ke must be > 0" }
        require(vd > 0) { "Vd must be > 0" }

        val cmaxSs = calculateCmaxSs(doseMg, tinf, tau, ke, vd)
        val cminSs = calculateCminSs(cmaxSs, ke, tau, tinf)

        val normalizedT = t % tau
        return if (normalizedT <= tinf) {
            val k0 = doseMg / tinf
            (k0 / (ke * vd)) * (1.0 - exp(-ke * normalizedT)) + cminSs * exp(-ke * normalizedT)
        } else {
            cmaxSs * exp(-ke * (normalizedT - tinf))
        }
    }

    /**
     * Generates a series of time-concentration points over multiple dosing intervals.
     */
    fun generateCurvePoints(
        doseMg: Double,
        tinf: Double,
        tau: Double,
        ke: Double,
        vd: Double,
        stepHr: Double = 0.05,
        intervalsToShow: Double = 2.5
    ): List<CurvePoint> {
        val totalDuration = tau * intervalsToShow
        val points = mutableListOf<CurvePoint>()
        var t = 0.0
        while (t <= totalDuration + 1e-9) {
            val c = calculateConcentration(doseMg, tinf, tau, ke, vd, t)
            points += CurvePoint(timeHr = t, concentrationMgL = c)
            t += stepHr
        }
        return points
    }

    /**
     * Solves for Ke numerically using bisection search so that the steady-state model
     * predicts measuredConc at sample time t.
     */
    fun solveKeForConcentration(
        doseMg: Double,
        tinf: Double,
        tau: Double,
        vd: Double,
        t: Double,
        measuredConc: Double,
        minKe: Double = 0.001,
        maxKe: Double = 1.0,
        maxIterations: Int = 100,
        tolerance: Double = 1e-6
    ): Double {
        require(measuredConc > 0) { "Measured concentration must be positive" }

        val cLow = calculateConcentration(doseMg, tinf, tau, minKe, vd, t)
        val cHigh = calculateConcentration(doseMg, tinf, tau, maxKe, vd, t)

        if (measuredConc > cLow || measuredConc < cHigh) {
            val formattedMeasured = "%.2f".format(measuredConc)
            val formattedMin = "%.2f".format(cHigh)
            val formattedMax = "%.2f".format(cLow)
            throw IllegalArgumentException(
                "Measured concentration ($formattedMeasured mg/L) is outside the valid range " +
                        "[$formattedMin - $formattedMax mg/L] achievable by the model for Ke in [$minKe - $maxKe /h]."
            )
        }

        var low = minKe
        var high = maxKe
        var mid = (low + high) / 2.0

        for (i in 0 until maxIterations) {
            if (high - low < tolerance) break
            mid = (low + high) / 2.0
            val cMid = calculateConcentration(doseMg, tinf, tau, mid, vd, t)
            if (cMid > measuredConc) {
                low = mid
            } else {
                high = mid
            }
        }
        return mid
    }

    /**
     * Calculates half-life (t1/2 = ln(2) / Ke).
     */
    fun calculateHalfLife(ke: Double): Double {
        require(ke > 0) { "Ke must be > 0" }
        return ln(2.0) / ke
    }

    /**
     * Calculates clearance (CL = Ke * Vd).
     */
    fun calculateClearance(ke: Double, vd: Double): Double {
        require(ke >= 0) { "Ke must be >= 0" }
        require(vd >= 0) { "Vd must be >= 0" }
        return ke * vd
    }

    /**
     * Calculates AUC over one dosing interval (AUCtau = Dose / CL).
     */
    fun calculateAucTau(doseMg: Double, clearance: Double): Double {
        require(clearance > 0) { "Clearance must be > 0" }
        return doseMg / clearance
    }

    /**
     * Calculates 24-hour AUC (AUC24 = AUCtau * 24 / tau).
     */
    fun calculateAuc24(aucTau: Double, tau: Double): Double {
        require(tau > 0) { "Dosing interval (tau) must be > 0" }
        return aucTau * 24.0 / tau
    }
}
