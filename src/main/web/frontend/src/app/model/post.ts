import { Comment } from './comment';
export class Post {

    public id: number;
    public name: string;
    public username: string;
    public caption: string;
    public location: string;
    public userImageId: number;
    public likes: number;
    public postedDate: Date;
    public commentList: Comment[];
}
