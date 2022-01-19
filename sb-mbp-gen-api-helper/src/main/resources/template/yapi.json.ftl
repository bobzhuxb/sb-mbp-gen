{
  "paths": {
    "${interUrl}?interNo=${interNo}": {
      "get": {
        "tags": [
          "${yapiTags}"
        ],
        "summary": "${interDescr}",
        "description": "",
        "parameters": ${params},
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$schema": "http://json-schema.org/draft-04/schema#",
              "type": "object",
              "properties": {
                "resultCode": {
                  "type": "string",
                  "description": "返回码（1：成功  2：失败）"
                },
                "errMsg": {
                  "type": "null",
                  "description": "错误信息"
                },
                "data": {
                  "type": "object",
                  "description": "实际数据",
                  "properties": ${results}
                }
              }
            }
          }
        }
      }
    }
  }
}