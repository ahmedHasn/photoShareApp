import { Post } from './post';

export class User {
    public id: number;
    public name: string;
    public username: string;
    public email: string;
    public password: string;
    public bio: string;
    public createdDate: Date;
    public posts: Post[];
    public likedPosts: Post[];
}
