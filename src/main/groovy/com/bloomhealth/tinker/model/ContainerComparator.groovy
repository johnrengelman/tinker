package com.bloomhealth.tinker.model

import com.bloomhealth.tinker.Container
import com.bloomhealth.tinker.DataBag

class ContainerComparator {

    static List<Diff> compare(Container containerA, Container containerB) {
        List<Diff> diffs = []

        containerA.dataBags.findAll { a ->
            !containerB.dataBags.find { it.name == a.name }
        }.each {
            diffs << new ExtraDataBag(name: it.name)
        }

        containerB.dataBags.findAll { b ->
            !containerA.dataBags.find { it.name == b.name }
        }.each {
            diffs << new MissingDataBag(name: it.name)
        }

        containerA.dataBags.each { a ->
            DataBag b = containerB.dataBags.find { it.name == a.name }
            if (b) {
                List<Diff> dataBagDiffs = DataBagComparator.compare(a, b)
                diffs.addAll(dataBagDiffs)
            }
        }
        return diffs
    }
}
