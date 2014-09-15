package com.bloomhealth.tinker.model

abstract class Diff {

}

abstract class DataBagDiff extends Diff {
    String name
}
class MissingDataBag extends DataBagDiff {
    String type = 'Missing DataBag'
}
class ExtraDataBag extends DataBagDiff {
    String type = 'Extra DataBag'
}

abstract class ItemDiff extends Diff {

    String dataBagName
    String name
}
class MissingItem extends ItemDiff {
    String type = 'Missing Item'
}
class ExtraItem extends ItemDiff {
    String type = 'Extra Item'
}

abstract class ItemKeyDiff extends Diff {

    String dataBagName
    String itemName
    String name
}

class MissingItemKey extends ItemKeyDiff {
    String type = 'Missing ItemKey'
}
class ExtraItemKey extends ItemKeyDiff {
    String type = 'Extra ItemKey'
}
