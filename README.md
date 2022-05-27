# API Document

> First, learn about: `Result` wrapper.

- All response has been wrapped to a Result object.
- Result has two properties which one is data and another is message.
- data: the object which you wanna to get.
- message: the error message.

> The following return values are wrapped by `Result` by default.
>
> The following APIs are **GET** methods by default.

- User

| uri          | param                     | return       | description   |
|--------------|---------------------------|--------------|---------------|
| `/user/{id}` |                           | User         | Find user     |
| `/user`      |                           | List\<User\> | Get all users |
| `/user/save` | email, username, password | User         | Save user     |

- Conversation

| uri                    | param                  | return          | description                                                      |
|------------------------|------------------------|-----------------|------------------------------------------------------------------|
| `/con/{id}`            |                        | Conversation    | Find conversation                                                |
| `/con/members/{conId}` |                        | List\<Integer\> | Get member list (userId only) of conversation                    |
| `/con/member/{id}`     |                        | Member          | Get member by id                                                 |
| `/con/memberBlur`      | conId, userId          | Member          | Get member by conId and userId                                   |
| `/con/save`            | name, creatorId        | Conversation    | Save conversation                                                |
| `/con/invite/{conId}`  | from, to               | Invite          | Invite other to join this conversation by providing two user ids |
| `/con/message/{conId}` | limit(default: 20)     | List\<Message\> | Find messages by conId and limit value                           |
| `/con/send`            | conId, userId, content | Message         | Send message                                                     |

- Invite

| uri                    | param | return         | description                             |
|------------------------|-------|----------------|-----------------------------------------|
| `/invite/approve/{id}` |       | Invite         | Approve the invitation                  |
| `/invite/refuse/{id}`  |       | Invite         | Refuse the invitation                   |
| `/invite/out/{userId}` |       | List\<Invite\> | Get all invitations for userId applied  |
| `/invite/in/{userId}`  |       | List\<Invite\> | Get all invitations for userId received |

- Message

| uri             | param | return  | description  |
|-----------------|-------|---------|--------------|
| `/message/{id}` |       | Message | Find Message |
