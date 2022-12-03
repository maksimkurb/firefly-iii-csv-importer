import {
  Configuration,
  ImportControllerApi,
  MapperControllerApi,
  ScriptControllerApi,
  UserControllerApi,
  InfoControllerApi,
} from "../api";

export const SERVER_BASE_URL =
  import.meta.env.SERVER_BASE_URL || "http://localhost:8123";

const apiConfig = new Configuration({
  basePath: "http://localhost:8123",
  baseOptions: {
    withCredentials: true,
  },
});

const userApi = new UserControllerApi(apiConfig);
const scriptApi = new ScriptControllerApi(apiConfig);
const mappingConfigApi = new MapperControllerApi(apiConfig);
const importApi = new ImportControllerApi(apiConfig);
const infoApi = new InfoControllerApi(apiConfig);

export { userApi, scriptApi, mappingConfigApi, importApi, infoApi };
