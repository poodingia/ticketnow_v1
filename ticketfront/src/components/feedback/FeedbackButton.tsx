import {Button} from "@/components/ui/button";
import { updateFeedback} from "@/api/feedback";
import {useMutation, useQueryClient} from "@tanstack/react-query";

interface ResolveButtonProps {
  id: string;
}

export default function ResolveButton({ id }: ResolveButtonProps) {
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (data: {id: string, status: string}) => updateFeedback(data.id, data.status),
    onSuccess: () => {
      queryClient.invalidateQueries({queryKey: ["feedback", id]}).then();
    },
  });

  const handleResolve = async () => {
    mutation.mutate({id, status: "RESOLVED"});
  }

  return (
    <Button variant="outline" onClick={handleResolve} className='mt-3'>
      Giải quyết
    </Button>
  )
}