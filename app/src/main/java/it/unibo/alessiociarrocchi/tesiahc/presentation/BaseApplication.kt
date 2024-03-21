package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.app.Application

class BaseApplication : Application() {
  val healthConnectManager by lazy {
      it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectManager(this)
  }
}
