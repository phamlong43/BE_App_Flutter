const SockJS = require('sockjs-client');
const Stomp = require('stompjs');

function connectUser(userId, token, conversationId) {
    // Sử dụng WebSocket thuần nếu backend KHÔNG có withSockJS, ngược lại dùng SockJS
    // Nếu backend đã thêm .withSockJS() trong WebSocketConfig, giữ nguyên dòng dưới:
    const socket = new SockJS('http://localhost:8080/ws');
    // Nếu backend KHÔNG có withSockJS, dùng dòng sau:
    // const socket = new WebSocket('ws://localhost:8080/ws');
    const stompClient = Stomp.over(socket);

    console.log(`Connecting User ${userId} to conversationId: ${conversationId}`);

    stompClient.connect(
        { Authorization: `Bearer ${token}` },
        (frame) => {
            console.log(`User ${userId} connected:`, frame);

            // Subscribe vào topic
            const subscription = stompClient.subscribe(
                `/topic/conversation/${conversationId}`,
                (message) => {
                    console.log(`User ${userId} received:`, JSON.parse(message.body));
                },
                (error) => {
                    console.error(`User ${userId} subscription error:`, error);
                }
            );

            // Gửi tin nhắn thử nghiệm
            setTimeout(() => {
                stompClient.send(
                    `/app/chat/${conversationId}`,
                    {},
                    JSON.stringify({
                        from: `user${userId}`,
                        to: userId === 1 ? 'user2' : 'user1',
                        content: `Xin chào từ User ${userId}`
                    })
                );
                console.log(`User ${userId} sent message`);
            }, 2000);
        },
        (error) => {
            console.error(`User ${userId} connection error:`, error);
        }
    );

    return stompClient;
}

const user1Token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzUwMDkwMzM0LCJleHAiOjE3NTAxNzY3MzR9.2L1Mbf9ojRjkY74H-d9Ds4EmL3qXdLcPAyJYs6oDJUd0F5V2Lfr10NJmlZuT7s37eEMURWIvhLtOhBTkT9UnHA';
const user2Token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzUwMDkwMzE4LCJleHAiOjE3NTAxNzY3MTh9.HPyDoPL-qR8vyp3PONa5ZdB0CP-TAFhRDUVIWZXqL82F51V2NUrCeU6G_mjarWIzWs07czzxsctU7le-gp_E5A';
const conversationId = 1; // Đúng conversationId từ database

const client1 = connectUser(1, user1Token, conversationId);
const client2 = connectUser(2, user2Token, conversationId);