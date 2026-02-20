import { useToast as useVueToast } from "vue-toastification";

export function useToast() {
  const toast = useVueToast();

  return {
    success: (msg) => toast.success(msg),
    warning: (msg) => toast.warning(msg),
    error: (msg) => toast.error(msg),
    info: (msg) => toast.info(msg),
  };
}
