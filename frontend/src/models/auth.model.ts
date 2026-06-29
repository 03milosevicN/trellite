export interface RegistrationRequestModel {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
}

export interface LoginRequestModel {
    email: string;
    password: string;
}
export interface LoginResponseModel {
    token: string;
    id: number;
}