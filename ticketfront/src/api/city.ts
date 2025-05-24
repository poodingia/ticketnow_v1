import {API_CRUD_URL} from "@/utils/constant";
import {City} from "@/model/city.model";
import axios from 'axios';

export const getALlCities = async (): Promise<City[]> => {
    const { data } = await axios.get(`${API_CRUD_URL}/city`);
    return data.data;
}