import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

import com.bloomhealth.tinker.TinkerService
import com.bloomhealth.tinker.TinkerModule
import ratpack.jackson.JacksonModule

ratpack {
    bindings {
        add new JacksonModule()
        add new TinkerModule()
    }
    handlers { TinkerService tinkerService ->
        get {
            render groovyTemplate("index.html", title: "Tinker")
        }
        prefix('api') {
            get('containers') {
                render(json(tinkerService.list()))
            }
            //TODO should be a POST
            get('containers/refresh') {
                tinkerService.refresh()
                render(json('ok'))
            }
            get('containers/:name') {
                def name = pathTokens['name']
                render(json(tinkerService.get(name)))
            }
            get('containers/:name/compare/:comp') {
                def name = pathTokens['name']
                def comp = pathTokens['comp']
                render(json(tinkerService.compareContainers(name, comp)))
            }
        }

        assets "public"
    }
}
