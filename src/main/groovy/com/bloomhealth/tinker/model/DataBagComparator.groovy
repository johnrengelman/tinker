package com.bloomhealth.tinker.model

import com.bloomhealth.tinker.DataBag
import com.bloomhealth.tinker.Item

class DataBagComparator {

    static List<Diff> compare(DataBag dataBagA, DataBag dataBagB) {
        List<Diff> diffs = []
        dataBagA.items.findAll { a ->
            !dataBagB.items.find { it.name == a.name }
        }.each {
            diffs << new ExtraItem(name: it.name, dataBagName: dataBagA.name)
        }

        dataBagB.items.findAll { b ->
            !dataBagA.items.find { it.name == b.name }
        }.each {
            diffs << new MissingItem(name: it.name, dataBagName: dataBagB.name)
        }

        dataBagA.items.each { a ->
            Item b = dataBagB.items.find { it.name == a.name }
            if (b) {
                List<ItemKeyDiff> itemDiffs = ItemComparator.compare(a, b)
                itemDiffs.each { it.dataBagName = dataBagA.name }
                diffs.addAll(itemDiffs)
            }
        }
        return diffs
    }
}
