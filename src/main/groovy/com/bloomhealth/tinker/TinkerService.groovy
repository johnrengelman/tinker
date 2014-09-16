package com.bloomhealth.tinker

import com.bloomhealth.tinker.model.ComparisonResult
import com.bloomhealth.tinker.model.ContainerComparator
import groovy.json.JsonSlurper

class TinkerService {

    private Map<String, Container> containers = [:]

    TinkerService() {
        loadContainers(rootDir).each {
            containers[it.name] = it
        }
    }

    private File getRootDir() {
        new File(System.getenv('DATABAG_HOME') ?: '/Users/rzienert/bloom/bloom-databags-temp')
    }

    private List<Container> loadContainers(File root) {
        root.list(new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                return !name.startsWith('.')
            }
        }).collect {
            loadContainer(root, it)
        }.asList()
    }

    private Container loadContainer(File root, String name) {
        File containerRoot = new File(root, name)
        new Container(name: name, dataBags: loadDataBags(containerRoot))
    }

    private List<DataBag> loadDataBags(File containerRoot) {
        containerRoot.list().collect {
            loadDataBag(containerRoot, it)
        }.asList()
    }

    private DataBag loadDataBag(File containerRoot, String name) {
        File dataBagRoot = new File(containerRoot, name)
        new DataBag(name: name, items: loadItems(dataBagRoot))
    }

    private List<Item> loadItems(File dataBagRoot) {
        dataBagRoot.list().collect {
            loadItem(dataBagRoot, it - '.json')
        }.asList()
    }

    private Item loadItem(File dataBagRoot, String name) {
        File itemFile = new File(dataBagRoot, "${name}.json")
        Map json = new JsonSlurper().parse(itemFile)
        new Item(name: name, id: json.id, itemKeys: loadItemKeys(itemFile))
    }

    private List<ItemKey> loadItemKeys(File itemFile) {
        Map json = new JsonSlurper().parse(itemFile)
        Set<String> keys = json.keySet()
        keys.remove('id')
        keys.collect {
            Map keyJson = json[it]
            new ItemKey(name: it, encrypted: keyJson.containsKey('encrypted_data'))
        }.asList()
    }

    void refresh() {
        containers.clear()
        loadContainers(rootDir).each {
            containers[it.name] = it
        }
    }

    List<String> list() {
        return containers.keySet().asList()
    }

    Container get(String name) {
        containers[name]
    }

    ComparisonResult compareContainers(String aContainer, String bContainer) {
        Container a = get(aContainer)
        Container b = get(bContainer)
        ComparisonResult result = new ComparisonResult()

        result.diffs.addAll(ContainerComparator.compare(a, b))
        return result
    }
}
