let ws = {}
let app = new Vue({
    el:'#app',
    data:{
        id:'',  //用户id
        message:'',  //向服务器发送的消息
    },
    methods:{
        //创建webSocket连接
        createSession(){
            //生成随机id，模拟用户唯一id
            this.id=Math.floor(Math.random()*100000+1000)

            if ("WebSocket" in window) {
                // alert("您的浏览器支持 WebSocket!");

                // 打开一个 web socket
                ws = new WebSocket("ws://localhost:8081/websocket/"+this.id);

                ws.onopen = function () {
                    // Web Socket 已连接上，使用 send() 方法发送数据
                    ws.send("发送数据");
                    alert("数据发送中...");
                };

                ws.onmessage = function (evt) {
                    //获取服务端发来的消息
                    alert(evt.data)
                };

                ws.onclose = function () {
                    // 关闭 websocket
                    alert("连接已关闭...");
                };
            }

            else {
                // 浏览器不支持 WebSocket
                alert("您的浏览器不支持 WebSocket!");
            }
        },

        //调用服务端向客户端发送消息
        recallMessage(){
            axios.post('/demo/message.shtml?id='+this.id).then(function (response) {
                console.log(response.data)
            })
        },

        //调用服务端向客户端发送json对象
        recallJson(){
            axios.post('/demo/json.shtml?id='+this.id).then(function (response) {
                console.log(response.data)
            })
        },

        //向客户端发送消息
        sendMessage(){
            ws.send(app.message)
        }
    },
    created(){
        //初始化建立socket连接
        this.createSession();
    }
})
