update `onyx-base-sdk` to version 1.4.3.7 or above


This Method may block thread, Do not invoke in UI thread, for more detail to see [DictionaryActivity](../app/sample/src/main/java/com/onyx/android/sample/DictionaryActivity.java)

```
    /**
     * 
     * @param context  
     * @param keyword
     * @return DictionaryQuery
     *         
     */
    public static DictionaryQuery queryKeyWord(Context context, String keyword)
```

**DictionaryQuery** 字段说明


| 字段 |  含义 |
|:--|--:|
| state | 查询结果状态值 |
| `List<DictionaryQuery.Dictionary>` | 多个字典查询结果的List |
| `DictionaryQuery.Dictionary` | 字典查询结果的实体 |
| state | 查询状态 |
| dictName | 字典名称 |
| keyword | 查询关键字 |
| explanation | 关键字释义 | 



**state** 的状态值说明


| 状态值 | 说明 |
|:--|--:|
| DICT_STATE_ERROR | 查询失败，字典未安装或发生异常 |
| DICT_STATE_PARAM_ERROR | 查询失败，参数错误 |
| DICT_STATE_QUERY_SUCCESSFUL | 查询成功 |
| DICT_STATE_QUERY_FAILED | 查询失败 |
| DICT_STATE_LOADING | 加载中 |
| DICT_STATE_NO_DATA | 未查询到数据 |


