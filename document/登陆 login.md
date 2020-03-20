## 登陆 /user/login

请求:登陆

email

password

返回:

{{data:"number code, string session"}}



## 基本信息 /user/getinfo

请求:基本信息

session

返回:

{{data:"string username, int user_class, int url_num, string tenresourse"}}



## 创建短链接 /createURL

请求:

session:0

original_url:

resourse_id:""

id_ttl:0 //零为无限

返回:

{{data : "int code, string resourse_id, int ttl"}}

## 查询创建记录 /user/getAllRecord

请求:

session:

返回:

{
​	data: {
​		"int code, int numOfRecord",
​		map: {
​			"string mini_url, string original_url, string creat_date, string dead_time"
​		}
​	}
}

