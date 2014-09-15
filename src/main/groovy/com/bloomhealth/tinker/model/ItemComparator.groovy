package com.bloomhealth.tinker.model

import com.bloomhealth.tinker.Item

class ItemComparator {

    static List<ItemKeyDiff> compare(Item itemA, Item itemB) {
        List<ItemKeyDiff> diffs = []
        itemA.itemKeys.findAll { a ->
            !itemB.itemKeys.find { it.name == a.name }
        }.each {
            diffs << new ExtraItemKey(name: it.name, itemName: itemA.name )
        }

        itemB.itemKeys.findAll { b ->
            !itemA.itemKeys.find { it.name == b.name }
        }.each {
            diffs << new MissingItemKey(name: it.name, itemName: itemA.name)
        }
        return diffs
    }
}
