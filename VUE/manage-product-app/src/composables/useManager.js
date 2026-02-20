import { ref } from "vue";
import axios from "axios";
import { useToast } from "@/composables/useToast";
import { useAuthStore } from "@/stores/auth";

const API_URL = "http://localhost:3001/managers";
const STORAGE_KEY = "admin-manager";

const manager = ref(null);

const savedManager = localStorage.getItem(STORAGE_KEY);
if (savedManager) {
  manager.value = JSON.parse(savedManager);
}

export function useManager() {
  const loading = ref(false);
  const toast = useToast();
  const authStore = useAuthStore();

  authStore.isLoggedIn = !!manager.value;

  async function loginManager(name) {
    if (!name.trim()) {
      toast.warning("이름을 입력해주세요");
      return;
    }

    loading.value = true;

    try {
      const { data } = await axios.get(`${API_URL}?name=${name}`);

      if (data.length > 0) {
        manager.value = data[0];
      } else {
        const res = await axios.post(API_URL, {
          name,
          createdAt: Date.now(),
        });
        manager.value = res.data;
      }

      localStorage.setItem(STORAGE_KEY, JSON.stringify(manager.value));
      authStore.isLoggedIn = true;

      toast.success(`${manager.value.name} 관리자님 로그인 완료`);
    } catch (e) {
      toast.error("관리자 등록 실패");
    } finally {
      loading.value = false;
    }
  }

  function logoutManager() {
    manager.value = null;
    localStorage.removeItem(STORAGE_KEY);

    authStore.isLoggedIn = false;
    toast.info("로그아웃 되었습니다");
  }

  return {
    manager,
    loading,
    loginManager,
    logoutManager,
  };
}
