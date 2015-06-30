package com.hida.play.redmart

import spock.lang.Specification

/**
 * Created by arief.hidayat on 30/06/2015.
 */
class SkiMapSpec extends Specification {
    def "should work on simple data"() {
        given: """
The following input:
4 4
4 8 7 3
2 5 9 3
6 3 2 5
4 4 1 6
"""
        SkiMap skiMap = new SkiMap()
        def input = ["4 4", "4 8 7 3", "2 5 9 3", "6 3 2 5", "4 4 1 6"]
        when:
        Result result = skiMap.process(input)
        then: "path length = 5"
        result.maxLength == 5
        and : "height diff = 8"
        result.maxVal == 8
        and : "path = 9->5->3->2->1"
        result.description == "9->5->3->2->1"
    }

    def "should work on real data"() {
        given: "data from http://s3-ap-southeast-1.amazonaws.com/geeks.redmart.com/coding-problems/map.txt"
        SkiMap skiMap = new SkiMap()
        String url = "http://s3-ap-southeast-1.amazonaws.com/geeks.redmart.com/coding-problems/map.txt"
        when:
        Result result = skiMap.process(url)
        then: "path length = 15"
        result.maxLength == 15
        and: "height diff = 1422"
        result.maxVal == 1422
        and : "path 1422->1412->1316->1304->1207->1162->965->945->734->429->332->310->214->143->0"
        result.description == "1422->1412->1316->1304->1207->1162->965->945->734->429->332->310->214->143->0"
    }
}
