package helpers

import javax.inject.Inject

class ViewHelpers @Inject()(val env: play.api.Environment,
                            val config: play.api.Configuration) {
}
